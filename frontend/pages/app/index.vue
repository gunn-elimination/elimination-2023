<template>
    <div class="h-full overflow-auto flex flex-col">
        <header class="flex sticky-header flex-row gap-8 p-8">
            <div class="my-auto flex hidden sm:block">
                <Avatar
                    ring
                    :user="userStore.currentUser"
                    v-if="userStore.currentUser"
                />
            </div>
            <h2 class="font-bold text-3xl text-center text-primary w-full align-middle my-auto">
                Dashboard
            </h2>
            <div class="my-auto flex gap-4">
                <KillCodeButton />
                <EliminateButton />
            </div>
        </header>

        <section class="p-8 bg-secondary text-secondary-content">
            <h2 class="font-bold text-xl flex flex-row gap-2 mb-3">
                <ViewfinderCircleIcon class="w-5 h-5 stroke-2 my-auto" />
                Your Target
            </h2>
            <div class="text-center flex flex-col items-center" v-if="userStore.target">
                <Avatar :user="userStore.target" large />
                <span class="font-bold text-lg">{{ userStore.target.forename }} {{ userStore.target.surname }}</span>
                <span class="text-sm"><!-- #4 • -->{{ userStore.target.eliminated.length }} kills</span>
            </div>
        </section>

        <section class="px-8 py-8 bg-base-200">
            <h3 class="font-semibold text-secondary mb-4">
                Restrictions for {{ currentTimeStore.time.toLocaleString({ weekday: 'long', month: 'long', day: '2-digit' }) }}:
            </h3>
            <div v-if="info" class="flex flex-col gap-2 px-4">
                <div v-if="info.announcement" class="flex gap-4">
                    <MegaphoneIcon class="h-6 w-6 text-primary flex-none" />
                    <strong>{{ info.announcement }}</strong>
                </div>
                <div v-if="info.restriction" class="flex gap-4">
                    <LockClosedIcon class="h-6 w-6 text-primary flex-none" />
                    {{ info.restriction }}
                </div>
            </div>
            <p v-else class="text-secondary text-center italic">
                There are no restrictions to display.
            </p>
        </section>

        <div class="px-4 pt-6 pb-12 sm:p-8 flex flex-col gap-4">
            <h3 class="font-semibold text-secondary">
                Elimination feed:
            </h3>
            <KillFeedBlock
                v-for="n in 10"
                :key="n"
                :item="{eliminated: userStore.currentUser, eliminator: userStore.currentUser, timestamp: '2023-01-27T11:37:16.345-08:00'}"
            />
        </div>
    </div>
</template>

<script lang="ts">
import {ChevronDownIcon, QrCodeIcon, ViewfinderCircleIcon} from "@heroicons/vue/24/outline/index.js";
import {MegaphoneIcon, LockClosedIcon} from "@heroicons/vue/24/solid/index.js";

export default {
    components: {ChevronDownIcon, QrCodeIcon, ViewfinderCircleIcon, MegaphoneIcon, LockClosedIcon}
};
</script>

<script setup lang="ts">
import {useUserStore} from '@/store/user';
import {useCurrentTimeStore} from '@/store/time';

const userStore = useUserStore();
const currentTimeStore = useCurrentTimeStore();

const info = computed(() => calendar[currentTimeStore.time.toString().slice(0, 10)])
</script>
