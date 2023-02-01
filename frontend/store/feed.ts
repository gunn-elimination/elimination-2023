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
        // TODO: work out how the backend is sending these events
        // TODO: make the sorting prettier
        if (Array.isArray(res)) feed.value = [...feed.value ?? [], ...res].sort((a, b) => new Date(b.timeStamp).valueOf() - new Date(a.timeStamp).valueOf());
        else feed.value = [...feed.value ?? [], res].sort((a, b) => new Date(b.timeStamp).valueOf() - new Date(a.timeStamp).valueOf());

        // Refresh all user endpoints to instantly update on kill
        userStore.refreshMe();
        userStore.refreshTarget();
        userStore.refreshEliminatedBy();
    })

    return {feed, error};
});
