<template>
    <aside class="drawer-side bg-base-200">
        <label for="my-drawer-2" class="drawer-overlay"></label>
        <nav class="text-primary">
            <h2 class="font-bold text-center text-4xl mt-8 mb-2">Elimination</h2>
            <ul class="menu p-4 w-80 gap-1 no-border-r border-primary/20">
                <li>
                    <NuxtLink
                        exact-active-class="active"
                        active-class="active"
                        href="/app/announcements"
                    >
                        <div class="indicator">
                            <!-- TODO: `v-if` conditional render this if there's no unreads -->
                            <span class="indicator-item badge badge-sm badge-error rounded-lg">
                                <!-- TODO: this is very very ugly, but stores and reactivity somewhat force my hand. -->
                                <!-- Think of a better way to do this perhaps -->
                                {{ announcementsStore.announcements.filter((announcement) => announcement.active && currentTimeStore.time >= announcement.startDate && currentTimeStore.time <= announcement.endDate && !announcementsStore.readIds.includes(announcement.id)).length }}
                            </span>
                            <MegaphoneIcon class="h-5 w-5 stroke-2"/>
                        </div>
                        Announcements
                    </NuxtLink>
                </li>
                <li>
                    <NuxtLink
                        exact-active-class="active"
                        active-class="active"
                        href="/app"
                    >
                        <HomeIcon class="w-5 h-5 stroke-2"/>
                        Home
                    </NuxtLink>
                </li>
                <li>
                    <NuxtLink
                        exact-active-class="active"
                        active-class="active"
                        href="/app/calendar"
                    >
                        <CalendarIcon class="w-5 h-5 stroke-2"/>
                        Calendar
                    </NuxtLink>
                </li>
                <li>
                    <NuxtLink
                        exact-active-class="active"
                        active-class="active"
                        href="/app/leaderboard"
                    >
                        <TrophyIcon class="w-5 h-5 stroke-2"/>
                        Leaderboard
                    </NuxtLink>
                </li>
                <li>
                    <NuxtLink
                        exact-active-class="active"
                        active-class="active"
                        href="/app/settings"
                    >
                        <Cog6ToothIcon class="w-5 h-5 stroke-2"/>
                        Settings
                    </NuxtLink>
                </li>
            </ul>
        </nav>
    </aside>
</template>

<script lang="ts">
import {HomeIcon, Cog6ToothIcon, MegaphoneIcon, CalendarIcon, TrophyIcon} from "@heroicons/vue/24/outline/index.js";

export default {
    components: {
        HomeIcon,
        Cog6ToothIcon,
        MegaphoneIcon,
        CalendarIcon,
        TrophyIcon,
    },
};
</script>

<script setup lang="ts">
import {useCurrentTimeStore} from '@/store/time';
import {useAnnouncementsStore} from '@/store/announcements';

const currentTimeStore = useCurrentTimeStore();
const announcementsStore = useAnnouncementsStore();
</script>
