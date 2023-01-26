<template>
    <div class="h-screen w-screen">
        <div class="drawer drawer-mobile">
            <input id="my-drawer-2" type="checkbox" class="drawer-toggle" />
            <div class="drawer-content pb-12 lg:pb-0 bg-base-100 flex flex-col">
                <NuxtPage class="h-full" />
                <BottomNav/>
            </div>
            <GameSidebar />
        </div>
    </div>
    <client-only>
        <InstallModal />
    </client-only>
    <KillCodeModal :code="code" />
</template>

<script setup lang="ts">
const config = useRuntimeConfig();

// TODO: it would be better to move this out of the layout, but the modal somewhat forces my hand.
const {data: code} = useFetch(`${config.public.apiUrl}/game/code`, {
    credentials: 'include',
    server: false
});
</script>
