import { defineStore } from 'pinia'
import {FetchResponse} from 'ofetch';
import {EliminationUser} from '~/utils/types';


export const useUserStore = defineStore('user', () => {
    const config = useRuntimeConfig();

    async function responseHandler({response}: { response: FetchResponse<any> }) {
        if (response._data.error) return navigateTo('/login');
        return response._data;
    }

    // TODO: middleware?
    const {data: currentUser, pending, refresh: refreshMe} = useFetch(`${config.public.apiUrl}/me`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });

    const {data: targetRaw, refresh: refreshTarget} = useFetch(`${config.public.apiUrl}/game/target`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });
    const target = computed(() => targetRaw.value && (targetRaw.value as {user: EliminationUser | null}).user);

    const {data: code} = useFetch(`${config.public.apiUrl}/game/code`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });

    const {data: eliminatedByRaw} = useFetch(`${config.public.apiUrl}/game/eliminatedBy`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });
    const eliminatedBy = computed(() => eliminatedByRaw.value && (eliminatedByRaw.value as {user: EliminationUser | null}).user);

    return {currentUser, target, code, eliminatedBy, pending, refreshMe, refreshTarget};
});
