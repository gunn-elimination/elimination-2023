<template>
    <!-- TODO: `v-if` conditional render this if there's no unreads -->
    <span class="indicator-item badge badge-sm badge-error rounded-lg">
        <!-- TODO: this is very very ugly, but stores and reactivity somewhat force my hand. -->
        <!-- Think of a better way to do this perhaps -->
        {{ announcementsStore.announcements?.filter((announcement) => announcement.active && currentTimeStore.time >= announcement.startDate && currentTimeStore.time <= announcement.endDate && !announcementsStore.readIds.includes(announcement.id)).length }}
    </span>
</template>

<script lang="ts">
export default {
    name: "GameSidebarUnreadAnnouncementsBadge"
}
</script>

<script setup lang="ts">
import {useCurrentTimeStore} from '@/store/time';
import {useAnnouncementsStore} from '@/store/announcements';

const currentTimeStore = useCurrentTimeStore();
const announcementsStore = useAnnouncementsStore();
</script>
