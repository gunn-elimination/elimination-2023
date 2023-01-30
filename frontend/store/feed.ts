import { defineStore } from 'pinia'
import {useEventSource} from '@vueuse/core';
import {useUserStore} from '@/store/user';
import type {EliminationFeedItem} from '@/utils/types';


export const useKillFeedStore = defineStore('feed', () => {
    const config = useRuntimeConfig();
    const userStore = useUserStore();

    const {data, error} = useEventSource(`${config.public.apiUrl}/game/eliminations`);
    const feed = ref<EliminationFeedItem[] | null>(null)
    watch(data, (raw) => {
        if (!raw) return;
        const res = JSON.parse(raw);

        // Handle both array and single-object events
        if (res.type === 'bulkKillfeed') feed.value = [...feed.value ?? [], ...res.value];
        else feed.value = [...feed.value ?? [], res.value];

        // Refresh all user endpoints to instantly update on kill
        userStore.refreshMe();
        userStore.refreshTarget();
        userStore.refreshEliminatedBy();
    })

    return {feed, error};
});
