import { defineStore } from 'pinia'
import { DateTime } from 'luxon';


export const useCurrentTimeStore = defineStore('currentTime', {
    state: () => {
        return { time: DateTime.now() }
    },
    actions: {
        updateTime() {
            this.time = DateTime.now()
        },
    },
});
