import { defineStore } from 'pinia'
import {useEventSource, useLocalStorage, useTimestamp} from '@vueuse/core';
import {useCurrentTimeStore} from '@/store/time';
import {Announcement} from '@/utils/types';


export const useAnnouncementsStore = defineStore('announcements', () => {
    const config = useRuntimeConfig();
    const currentTime = useTimestamp(); // TODO: use current time store for this?

    // Read announcements ID management
    const readIds = useLocalStorage<number[]>('elim-read-announcements', [], {
        writeDefaults: true
    });
    function markRead(id: number) {
        readIds.value = [...readIds.value, id];
    }

    const {data} = useEventSource(`${config.public.apiUrl}/announcements`);
    const announcements = computed(() => data.value && JSON.parse(data.value));
    const filteredAnnouncements = computed(() => announcements.value?.filter((announcement: Announcement) => announcement.active
        && currentTime.value >= announcement.startDate
        && currentTime.value <= announcement.endDate
        && !readIds.value.includes(announcement.id)))

    return {announcements, filteredAnnouncements, readIds, markRead};
});
