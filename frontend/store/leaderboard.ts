import { defineStore } from 'pinia'
import {useEventSource} from '@vueuse/core';


export const useLeaderboardStore = defineStore('leaderboard', () => {
    const config = useRuntimeConfig();

    const {data, error} = useEventSource(`${config.public.apiUrl}/game/scoreboard`);
    const leaderboard = computed(() => data.value && JSON.parse(data.value).users);
    const participants = computed(() => data.value && JSON.parse(data.value).numParticipants);

    return {leaderboard, participants, error};
});
