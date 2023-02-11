import { defineStore } from 'pinia'
import {useCurrentTimeStore} from '@/store/time';
import type {FetchResponse} from 'ofetch';
import type {EliminationUser, MeEliminationUser} from '@/utils/types';


export const useUserStore = defineStore('user', () => {
    const config = useRuntimeConfig();
    const pending = ref(true);

    const currentTimeStore = useCurrentTimeStore();

    function createResponseHandler(ignoreErrorAfterGameEnd = true) {
        return ({response}: { response: FetchResponse<any> }) => {
            // Ignore auth errors if the option is set and the game has already ended
            if (response._data.error && (!ignoreErrorAfterGameEnd || !currentTimeStore.gameEnded)) return navigateTo('/login');
            if (currentTimeStore.gameEnded) return null;

            pending.value = false;
            return response._data;
        }
    }

    // TODO: middleware?
    const {data: currentUser, refresh: refreshMe} = useFetch<MeEliminationUser>(`${config.public.apiUrl}/me`, {
        credentials: 'include',
        server: false,
        redirect: 'manual',
        onResponse: createResponseHandler(false)
    });

    const {data: targetRaw, refresh: refreshTarget} = useFetch<{user: EliminationUser | null}>(`${config.public.apiUrl}/game/target`, {
        credentials: 'include',
        server: false,
        redirect: 'manual',
        onResponse: createResponseHandler()
    });
    const target = computed(() => targetRaw.value && targetRaw.value.user);

    const {data: code} = useFetch<string>(`${config.public.apiUrl}/game/code`, {
        credentials: 'include',
        server: false,
        redirect: 'manual',
        onResponse: createResponseHandler()
    });

    const {data: eliminatedByRaw, refresh: refreshEliminatedBy} = useFetch<{user: EliminationUser | null}>(`${config.public.apiUrl}/game/eliminatedBy`, {
        credentials: 'include',
        server: false,
        redirect: 'manual',
        onResponse: createResponseHandler()
    });
    const eliminatedBy = computed(() => eliminatedByRaw.value && eliminatedByRaw.value.user);

    return {
        currentUser, target, code, eliminatedBy, pending,
        refreshMe, refreshTarget, refreshEliminatedBy
    };
});
