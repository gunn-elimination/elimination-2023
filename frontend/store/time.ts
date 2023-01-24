import { defineStore } from 'pinia'
import { DateTime } from 'luxon';


export const useCurrentTimeStore = defineStore('currentTime', () => {
    const time = ref(DateTime.now());
    const updateTime = () => time.value = DateTime.now();

    return {time, updateTime};
});
