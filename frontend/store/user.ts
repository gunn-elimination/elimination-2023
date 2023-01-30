import { defineStore } from 'pinia'
import type {FetchResponse} from 'ofetch';
import type {EliminationUser, MeEliminationUser} from '@/utils/types';


export const useUserStore = defineStore('user', () => {
    const config = useRuntimeConfig();

    async function responseHandler({response}: { response: FetchResponse<any> }) {
        if (response._data.error) return navigateTo('/login');
        return response._data;
    }

    // TODO: middleware?
    const {data: currentUser, pending, refresh: refreshMe} = useFetch<MeEliminationUser>(`${config.public.apiUrl}/me`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });

    const {data: targetRaw, refresh: refreshTarget} = useFetch<{user: EliminationUser | null}>(`${config.public.apiUrl}/game/target`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });
    const target = computed(() => targetRaw.value && targetRaw.value.user);

    const {data: code} = useFetch<string>(`${config.public.apiUrl}/game/code`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });

    const {data: eliminatedByRaw, refresh: refreshEliminatedBy} = useFetch<{user: EliminationUser | null}>(`${config.public.apiUrl}/game/eliminatedBy`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });
    const eliminatedBy = computed(() => eliminatedByRaw.value && eliminatedByRaw.value.user);

    return {
        currentUser, target, code, eliminatedBy, pending,
        refreshMe, refreshTarget, refreshEliminatedBy
    };
});
