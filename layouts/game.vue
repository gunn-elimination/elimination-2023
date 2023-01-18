<template>
  <div class="h-screen w-screen">
    <div class="drawer drawer-mobile">
      <input id="my-drawer-2" type="checkbox" class="drawer-toggle" />
      <div class="drawer-content pb-12 lg:pb-0 bg-base-100 flex flex-col">
        <NuxtPage class="h-full" />
        <btm-nav />
      </div>
      <div class="drawer-side">
        <label for="my-drawer-2" class="drawer-overlay"></label>
        <ul
          class="
            menu
            p-4
            w-80
            gap-1
            bg-base-200
            no-border-r border-primary/20
            text-primary
          "
        >
          <!-- Sidebar content here -->
          <h2 class="font-display text-center text-3xl my-4">Elimination</h2>
          <li>
            <NuxtLink
              exact-active-class="active"
              active-class="active"
              href="/app/announcements"
              ><div class="indicator">
                <span class="indicator-item badge badge-sm badge-error">2</span>
                <MegaphoneIcon class="h-5 w-5 stroke-2" />
              </div>
              Announcements</NuxtLink
            >
          </li>
          <li>
            <NuxtLink
              exact-active-class="active"
              active-class="active"
              href="/app"
              ><HomeIcon class="w-5 h-5 stroke-2" />Home</NuxtLink
            >
          </li>
          <li>
            <NuxtLink
              exact-active-class="active"
              active-class="active"
              href="/app/calendar"
              ><CalendarIcon class="w-5 h-5 stroke-2" />Calendar</NuxtLink
            >
          </li>
          <li>
            <NuxtLink
              exact-active-class="active"
              active-class="active"
              href="/app/leaderboard"
              ><TrophyIcon class="w-5 h-5 stroke-2" />Leaderboard</NuxtLink
            >
          </li>
          <li>
            <NuxtLink
              exact-active-class="active"
              active-class="active"
              href="/app/settings"
              ><Cog6ToothIcon class="w-5 h-5 stroke-2" />Settings</NuxtLink
            >
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>


<script>
import {
  HomeIcon,
  Cog6ToothIcon,
  MegaphoneIcon,
  CalendarIcon,
  TrophyIcon,
} from "@heroicons/vue/24/outline/index.js";

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

<style>
</style>