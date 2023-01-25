<template>
    <Head>
        <Title>App | Gunn Elimination</Title>
    </Head>

    <div v-if="pending" class="flex gap-3 flex-grow items-center justify-center text-secondary text-lg font-semibold">
        <Spinner /> Loading app...
    </div>
    <NuxtPage v-else :currentUser="currentUser" />
</template>

<script setup lang="ts">
import {useDocumentVisibility} from '@vueuse/core';
import {useCurrentTimeStore} from '@/store/time';
import type {FetchResponse} from 'ofetch';

const config = useRuntimeConfig();

definePageMeta({
    layout: "game",
    // middleware: "elim-auth"
});

// Update current time store on interval
const currentTimeStore = useCurrentTimeStore();
let currentTimeInterval: NodeJS.Timer;

onMounted(() => {
    currentTimeInterval = setInterval(() => currentTimeStore.updateTime(), 1000);
});
onUnmounted(() => {
    clearInterval(currentTimeInterval);
});

// Scan for service worker updates on page visibility
const pageVisibility = useDocumentVisibility();
watch(pageVisibility, () => navigator.serviceWorker.getRegistration().then(res => res?.update()));

async function responseHandler({response}: { response: FetchResponse<any> }) {
    if (response._data.error) return navigateTo('/login');
    return response._data;
}

// TODO: middleware?
const {data: currentUser, pending} = useFetch(`${config.public.apiUrl}/me`, {
    credentials: 'include',
    server: false,
    onResponse: responseHandler
});
</script>
