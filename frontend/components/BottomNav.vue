<template>
    <div class="btm-nav lg:hidden">
        <NuxtLink exact-active-class="active" active-class="active" href="/app/profile" class="text-primary">
            <UserIcon class="h-5 w-5 stroke-2"/>
        </NuxtLink>
        <NuxtLink exact-active-class="active" active-class="active" href="/app/calendar" class="text-primary">
            <CalendarIcon class="h-5 w-5 stroke-2"/>
        </NuxtLink>
        <NuxtLink exact-active-class="active" active-class="active" href="/app" class="text-primary">
            <HomeIcon class="h-5 w-5 stroke-2"/>
        </NuxtLink>
        <NuxtLink exact-active-class="active" active-class="active" href="/app/leaderboard" class="text-primary">
            <TrophyIcon class="h-5 w-5 stroke-2"/>
        </NuxtLink>
        <NuxtLink exact-active-class="active" active-class="active" href="/app/announcements" class="text-primary">
            <div class="indicator">
                <client-only>
                    <GameSidebarUnreadAnnouncementsBadge />
                </client-only>
                <MegaphoneIcon class="h-5 w-5 stroke-2"/>
            </div>
        </NuxtLink>
    </div>
</template>

<script setup lang="ts">
import {HomeIcon, UserIcon, MegaphoneIcon, CalendarIcon, TrophyIcon} from "@heroicons/vue/24/outline/index.js";
</script>
