<template>
    <div class="h-full overflow-auto flex flex-col">
        <h1 class="title sticky-header p-8">Profile</h1>
        <div class="p-8 bg-secondary text-secondary-content">
            <div class="container flex gap-4 sm:gap-8">
                <Avatar
                    :user="currentUser"
                    large
                />

                <div class="flex flex-col">
                    <h2 class="font-bold text-2xl mb-1">
                        {{ currentUser.forename }} {{ currentUser.surname }}
                    </h2>
                    <span>{{ currentUser.email }}</span>
                    <span><!-- #4 • -->{{ currentUser.eliminated.length }} eliminations</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {EliminationUser} from '@/utils/types';

export default {
    props: {
        currentUser: Object as PropType<EliminationUser>,
    }
};
</script>
