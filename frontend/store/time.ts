import { defineStore } from 'pinia'
import { DateTime } from 'luxon';


const gameEndDate = DateTime.fromISO('2023-02-11', {zone: 'America/Los_Angeles'});

export const useCurrentTimeStore = defineStore('currentTime', () => {
    const time = ref(DateTime.now());

    const updateTime = () => time.value = DateTime.now();
    const gameEnded = computed(() => time.value.toMillis() > gameEndDate.toMillis());

    return {time, updateTime, gameEnded};
});
