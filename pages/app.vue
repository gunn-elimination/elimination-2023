<template>
    <Head>
        <Title>App | Gunn Elimination</Title>
    </Head>

    <div v-if="pending">
        <!-- TODO: style this better -->
        Loading...
    </div>
    <NuxtPage v-else :currentUser="currentUser" />
</template>

<script setup lang="ts">
import type {FetchResponse} from 'ofetch';

definePageMeta({
    layout: "game",
    // middleware: "elim-auth"
});

async function responseHandler({response}: { response: FetchResponse<any> }) {
    if (response._data.error) return navigateTo('/login');
    return response._data;
}

// TODO: middleware?
const {data: currentUser, pending} = useFetch('https://xz.ax/me', {
    credentials: 'include',
    server: false,
    onResponse: responseHandler
});
</script>
