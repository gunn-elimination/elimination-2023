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
                            <client-only>
                                <GameSidebarUnreadAnnouncementsBadge />
                            </client-only>
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
                        href="/app/profile"
                    >
                        <UserIcon class="w-5 h-5 stroke-2"/>
                        Profile
                    </NuxtLink>
                </li>
            </ul>
        </nav>
    </aside>
</template>

<script lang="ts">
import {HomeIcon, UserIcon, MegaphoneIcon, CalendarIcon, TrophyIcon} from "@heroicons/vue/24/outline/index.js";

export default {
    components: {
        HomeIcon,
        UserIcon,
        MegaphoneIcon,
        CalendarIcon,
        TrophyIcon,
    },
};
</script>
