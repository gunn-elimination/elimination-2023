<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Announcements</h1>

        <section class="card w-full pt-6 pb-12 px-6 sm:px-8 gap-2">
            <div v-if="store.error" class="flex flex-col">
                <p class="mb-3">An error occurred fetching announcements. Check the console for more info.</p>
                <code class="rounded-lg p-5 bg-base-200 font-mono text-secondary whitespace-pre-wrap">
                    {{ store.error.type }} {{ store.error }}
                </code>
            </div>
            <div v-else-if="!store.announcements" class="flex gap-3 items-center text-secondary text-lg">
                <Spinner /> Loading announcements...
            </div>
            <!-- TODO: message for when there are no announcements -->
            <div v-for="announcement in store.announcements" :key="announcement.id">
                <Announcement
                    :title="announcement.title"
                    :body="announcement.body"
                    :start-date="DateTime.fromMillis(announcement.startDate).toLocaleString()"
                    :id="announcement.id"
                />
            </div>
        </section>
    </div>
</template>

<script setup lang="ts">
import {DateTime} from 'luxon';
import {useAnnouncementsStore} from '@/store/announcements';

const store = useAnnouncementsStore();
</script>
