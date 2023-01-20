<template>
    <Head>
        <Title>App | Gunn Elimination</Title>
    </Head>

    <NuxtPage />
</template>

<script setup lang="ts">
definePageMeta({
    layout: "game",
    middleware: "elim-auth"
});
</script>
