<template>
    <div class="h-full overflow-auto flex flex-col">
        <div class="flex sticky-header flex-row gap-8 p-8">
            <div class="my-auto flex hidden sm:block">
                <Avatar
                    ring
                    :user="store.currentUser"
                    v-if="store.currentUser"
                />
            </div>
            <h2 class="font-bold text-3xl text-center text-primary w-full align-middle my-auto">
                Dashboard
            </h2>
            <div class="my-auto flex gap-4">
                <KillCodeButton />
                <EliminateButton />
            </div>
        </div>

        <div class="p-8 bg-secondary text-secondary-content">
            <h2 class="font-bold text-xl flex flex-row gap-2 mb-2">
                <ViewfinderCircleIcon class="w-5 h-5 stroke-2 my-auto" />
                Your Target
            </h2>
            <div class="text-center flex flex-col items-center" v-if="store.target">
                <Avatar :user="store.target" large />
                <span class="font-bold text-lg">{{ store.target.forename }} {{ store.target.surname }}</span>
                <span class="text-sm"><!-- #4 • -->{{ store.target.eliminated.length }} kills</span>
            </div>
        </div>

        <!--
        <div class="px-4 py-6 sm:p-8 flex flex-col gap-4">
            <KillFeedBlock v-for="n in 10" :key="n"/>
        </div>
        -->
    </div>
</template>

<script lang="ts">
import {ChevronDownIcon, QrCodeIcon, ViewfinderCircleIcon} from "@heroicons/vue/24/outline/index.js";

export default {
    components: {ChevronDownIcon, QrCodeIcon, ViewfinderCircleIcon}
};
</script>

<script setup lang="ts">
import {useUserStore} from '@/store/user';

const store = useUserStore();
</script>
