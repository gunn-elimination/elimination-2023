<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Leaderboard</h1>
        <div class="p-8 bg-secondary text-secondary-content">
            <h2 class="font-bold text-xl flex flex-row gap-2">
                You
            </h2>
            <div class="text-center flex flex-col">
                <Avatar
                    class="mb-2 mx-auto"
                    :user="currentUser"
                    large
                />

                <span class="font-bold text-lg">{{ currentUser.forename }} {{ currentUser.surname }}</span>
                <span class="text-sm"><!-- #4 • -->{{ currentUser.eliminated.length }} eliminations</span>
            </div>
        </div>
        <div class="px-6 sm:px-12 mt-6 pb-12">
            <client-only>
                <Leaderboard />
            </client-only>
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '~/utils/types';

export default {
    props: {
        currentUser: Object as PropType<EliminationUser>
    }
}
</script>
