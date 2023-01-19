<template>
  <div v-if="loading" class="grid h-screen w-screen place-items-center">
    <p class="text-5xl sm:text-8xl text-transparent bg-gradient-to-br from-primary to-accent bg-clip-text font-bold">loading...</p>
  </div>
  <div v-else>
    <div class="h-screen w-screen">
        <div class="drawer drawer-mobile">
            <input id="my-drawer-2" type="checkbox" class="drawer-toggle"/>
            <div class="drawer-content pb-12 lg:pb-0 bg-base-100 flex flex-col">
                <NuxtPage class="h-full"/>
                <BottomNav/>
            </div>
            <GameSidebar />
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

import auth from "../middleware/elim-auth.js";

export default {
  components: {
    HomeIcon,
    Cog6ToothIcon,
    MegaphoneIcon,
    CalendarIcon,
    TrophyIcon,
  }, 
  data() {
    return {
      loading: true
    }
  },
  async created() {
    if (typeof window !== 'undefined') {
      await auth();
      this.loading = false;
    }
  }
};
</script>

<style>

</style>
