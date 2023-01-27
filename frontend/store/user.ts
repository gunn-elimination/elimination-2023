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
    const {data: currentUser, pending} = useFetch(`${config.public.apiUrl}/me`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });

    const {data: target} = useFetch(`${config.public.apiUrl}/game/target`, {
        credentials: 'include',
        server: false,
        onResponse: responseHandler
    });

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
    const eliminatedBy = computed(() => eliminatedByRaw && (eliminatedByRaw.value as {user: EliminationUser}).user);

    return {currentUser, target, code, eliminatedBy, pending};
});
