<template>
    <div class="h-full overflow-auto flex flex-col">
        <GameEnded v-if="true" />
        <template v-else>
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

            <PlayerSection :user="userStore.target" v-if="userStore.target">
                <ViewfinderCircleIcon class="w-5 h-5 stroke-2 my-auto" />
                Your Target
            </PlayerSection>
            <PlayerSection :user="userStore.eliminatedBy" v-else-if="userStore.eliminatedBy">
                <SkullIcon class="w-5 h-5 stroke-2 my-auto" />
                You were eliminated by
            </PlayerSection>

            <section class="px-8 py-8 bg-base-200">
                <h3 class="font-semibold text-secondary mb-4">
                    Restrictions for {{ currentTimeStore.time.toLocaleString({weekday: 'long', month: 'long', day: 'numeric'}) }}:
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
        </template>

        <section class="px-4 pt-6 pb-12 sm:p-8">
            <div v-if="killFeedStore.error" class="flex flex-col">
                <p class="mb-3">An error occurred fetching the elimination feed. Check the console for more info.</p>
                <code class="rounded-lg p-5 bg-base-200 font-mono text-secondary whitespace-pre-wrap">
                    {{ killFeedStore.error.type }} {{ killFeedStore.error }}
                </code>
            </div>
            <div v-else-if="!killFeedStore.feed" class="flex gap-3 items-center text-secondary text-lg">
                <Spinner /> Loading elimination feed...
            </div>
            <div class="flex flex-col gap-4" v-else>
                <h3 class="font-semibold text-secondary">
                    Elimination feed:
                </h3>
                <KillFeedBlock
                    v-for="item in killFeedStore.feed"
                    :key="item.timeStamp + item.eliminator.email"
                    :item="item"
                />
            </div>
        </section>
    </div>
</template>

<script setup lang="ts">
import {useUserStore} from '@/store/user';
import {useCurrentTimeStore} from '@/store/time';
import {useKillFeedStore} from '@/store/feed';
import {ViewfinderCircleIcon} from "@heroicons/vue/24/outline/index.js";
import {MegaphoneIcon, LockClosedIcon} from "@heroicons/vue/24/solid/index.js";

const userStore = useUserStore();
const currentTimeStore = useCurrentTimeStore();
const killFeedStore = useKillFeedStore();

const info = computed(() => calendar[currentTimeStore.time.toString().slice(0, 10)]);
</script>
