<template>
    <span
        v-if="announcementsStore.filteredAnnouncements?.length"
        class="indicator-item badge badge-sm badge-error rounded-lg"
    >
        {{ announcementsStore.filteredAnnouncements.length }}
    </span>
</template>

<script lang="ts">
export default {
    name: "GameSidebarUnreadAnnouncementsBadge"
}
</script>

<script setup lang="ts">
import {useAnnouncementsStore} from '@/store/announcements';

const announcementsStore = useAnnouncementsStore();
</script>
