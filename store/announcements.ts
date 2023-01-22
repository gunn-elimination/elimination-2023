import { defineStore } from 'pinia'


export const useAnnouncementsStore = defineStore('announcements', () => {
    const {data: announcements} = useFetch('https://xz.ax/announcements');

    // Read announcements ID management
    const readIds = ref(tryParseReadIds());
    function markRead(id: number) {
        readIds.value = [...readIds.value, id];
        localStorage.setItem('elim-read-announcements', JSON.stringify(readIds.value));
    }

    return {announcements, readIds, markRead};
});

// Tries to fetch the array of read announcements from `LocalStorage`, writing the property if it does
// not exist.
function tryParseReadIds() {
    try {
        return JSON.parse(localStorage.getItem('elim-read-announcements') ?? '');
    } catch (e) {
        localStorage.setItem('elim-read-announcements', '[]');
        return [];
    }
}
