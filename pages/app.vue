<template>
    <Head>
        <Title>App | Gunn Elimination</Title>
        <Meta name="description" content="Kill or be killed. It's Gunn Elimination 2023!" />
    </Head>

    <GameHasNotBegun />
    <!-- ENABLE THIS AFTER EVERYTHING IS DONE --->
    <!--<NuxtPage/>--->
</template>

<script>
definePageMeta({
    layout: "game",
    middleware: "elim-auth"
});
</script>
