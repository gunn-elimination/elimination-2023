<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Announcements</h1>

        <div v-for="announcement in announcements" :key="announcement.id" class="card w-full pt-6 pb-12 px-8">
            <!-- TODO: date formatting? -->
            <!-- TODO: only display if active && after start time && before end time -->
            <Announcement
                v-if="announcement.active"
                :title="announcement.title"
                :body="announcement.body"
                :start-date="new Date(announcement.startDate).toLocaleDateString()"
            />
        </div>

        {{ currentTime.time.toLocaleString(DateTime.DATE_HUGE) }}
    </div>
</template>

<script setup lang="ts">
import {useCurrentTimeStore} from '@/store/time';
import {DateTime} from 'luxon';

const currentTime = useCurrentTimeStore();
const {data: announcements} = useFetch('https://xz.ax/announcements');
</script>
