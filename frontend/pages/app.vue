<template>
    <Head>
        <Title>App | Gunn Elimination</Title>
    </Head>

    <div v-if="userStore.pending" class="flex gap-3 flex-grow items-center justify-center text-secondary text-lg font-semibold">
        <Spinner /> Loading app...
    </div>
    <NuxtPage v-else />
</template>

<script setup lang="ts">
import {useDocumentVisibility} from '@vueuse/core';
import {useCurrentTimeStore} from '@/store/time';
import {useUserStore} from '~/store/user';

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

const userStore = useUserStore();
</script>
