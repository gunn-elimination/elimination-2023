<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Announcements</h1>

        <div v-for="announcement in store.announcements" :key="announcement.id" class="card w-full pt-6 pb-12 px-8">
            <!-- TODO: date formatting? -->
            <Announcement
                v-if="announcement.active && currentTime.time >= announcement.startDate && currentTime.time <= announcement.endDate"
                :title="announcement.title"
                :body="announcement.body"
                :start-date="DateTime.fromMillis(announcement.startDate).toLocaleString()"
                :id="announcement.id"
            />
        </div>
    </div>
</template>

<script setup lang="ts">
import {DateTime} from 'luxon';
import {useCurrentTimeStore} from '@/store/time';
import {useAnnouncementsStore} from '@/store/announcements';

const currentTime = useCurrentTimeStore();
const store = useAnnouncementsStore();
</script>
