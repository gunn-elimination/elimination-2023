import { defineStore } from 'pinia'
import {useLocalStorage} from '@vueuse/core';


export const useAnnouncementsStore = defineStore('announcements', () => {
    const {data: announcements} = useFetch('https://xz.ax/announcements');

    // Read announcements ID management
    const readIds = useLocalStorage<number[]>('elim-read-announcements', [], {
        writeDefaults: true
    });
    function markRead(id: number) {
        readIds.value = [...readIds.value, id];
    }

    return {announcements, readIds, markRead};
});
