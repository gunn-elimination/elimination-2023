import { defineStore } from 'pinia'
import {useEventSource} from '@vueuse/core';


export const useKillFeedStore = defineStore('feed', () => {
    const config = useRuntimeConfig();

    const {data, error} = useEventSource(`${config.public.apiUrl}/game/eliminations`);
    const feed = computed(() => data.value && JSON.parse(data.value));

    return {feed, error};
});
