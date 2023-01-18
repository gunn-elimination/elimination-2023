<template>

<div class="btm-nav lg:hidden">
  <NuxtLink exact-active-class="active" active-class="active" href="/app/settings" class="text-primary">
    <Cog6ToothIcon class="h-5 w-5 stroke-2"/>
  </NuxtLink>
  <NuxtLink exact-active-class="active" active-class="active" href="/app/calendar" class="text-primary">
    <CalendarIcon class="h-5 w-5 stroke-2" />
  </NuxtLink>
  <NuxtLink exact-active-class="active" active-class="active" href="/app" class="text-primary">
    <HomeIcon class="h-5 w-5 stroke-2" />
  </NuxtLink>
  <NuxtLink exact-active-class="active" active-class="active" href="/app/leaderboard" class="text-primary">
    <TrophyIcon class="h-5 w-5 stroke-2" />
  </NuxtLink>
  <NuxtLink exact-active-class="active" active-class="active" href="/app/announcements" class="text-primary">
  <div class="indicator">
      <span v-if="false" class="indicator-item badge badge-sm badge-error">2</span> 
  <MegaphoneIcon class="h-5 w-5 stroke-2"/>
  </div>
  </NuxtLink>
</div>

</template>

<script>
import { HomeIcon, Cog6ToothIcon, MegaphoneIcon, CalendarIcon, TrophyIcon } from "@heroicons/vue/24/outline/index.js";

export default {
    components: {
        HomeIcon, Cog6ToothIcon, MegaphoneIcon, CalendarIcon, TrophyIcon
    }
}
</script>
