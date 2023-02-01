<template>
    <div class="card bg-base-300 border border-gray-300/10">
        <div class="card-body px-6">
            <h2 class="card-title">
                {{ title }} | {{ startDate }}
                <span class="badge badge-primary" v-if="!store.readIds.includes(id)">NEW</span>
            </h2>
            <p class="whitespace-pre-wrap text-secondary">
                {{ body }}
            </p>
            <div class="card-actions justify-end" v-if="!store.readIds.includes(id)">
                <button
                    class="btn btn-sm btn-primary"
                    @click="() => store.markRead(id)"
                >
                    mark as read
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import {useAnnouncementsStore} from '@/store/announcements';

defineProps({
    title: String,
    body: String,
    startDate: String,
    id: Number
});

const store = useAnnouncementsStore();
</script>
