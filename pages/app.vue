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
definePageMeta({
    layout: "game",
    // middleware: "elim-auth"
});

const {data: currentUser, pending} = useFetch('https://xz.ax/me', {
    credentials: 'include',
    server: false,
    async onResponse({response}) {
        console.log(response._data.error)
        if (response._data.error) return navigateTo('/login');
        return response._data;
    }
});
</script>
