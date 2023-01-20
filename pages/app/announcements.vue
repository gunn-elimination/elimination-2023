<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Announcements</h1>
        <GameHasNotBegun />
        <!--
        <div class="px-8">
            <div class="card card-compact w-full">
                <Announcement
                    title="Example Announcement"
                    body="Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement."
                    start-date="January 26, 2023"
                    end-date="January 26, 2023"
                    unread
                />

                <Announcement
                    title="Example Announcement"
                    body="Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement."
                    start-date="January 25, 2023"
                    end-date="January 25, 2023"
                    unread
                />

                <div v-for="announcement in announcements" :key="announcement.title + announcement.body" class="card card-compact w-full">
                    <Announcement
                        title="Example Announcement"
                        body="Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement. Long announcement."
                        start-date="January 21, 2023"
                        end-date="January 21, 2023"
                    />
                </div>
            </div>
        </div>
        -->
    </div>
</template>

<script setup>
const {data: announcements} = useFetch('https://xz.ax/announcements');
</script>
