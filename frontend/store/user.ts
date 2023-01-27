import { defineStore } from 'pinia'
import {FetchResponse} from 'ofetch';


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

    return {currentUser, target: currentUser, code, pending};
});
