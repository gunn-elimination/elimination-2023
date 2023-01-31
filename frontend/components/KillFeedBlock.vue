<template>
    <div class="card bg-base-300 border border-gray-300/20">
        <div class="card-body">
            <h2 class="card-title text-base flex-wrap gap-y-0.5">
                <span>
                    <Avatar :user="item.eliminator" /> {{ item.eliminator.forename }} {{ item.eliminator.surname }}
                </span>
                eliminated
                <span>
                    <Avatar :user="item.eliminated" /> {{ item.eliminated.forename }} {{ item.eliminated.surname }}
                </span>
            </h2>
            <p class="text-secondary text-sm">
                <!-- TODO: make this the same datetime object -->
                {{ DateTime.fromISO(item.timeStamp).toLocaleString({ weekday: 'long', month: 'long', day: '2-digit' }) }},
                {{ DateTime.fromISO(item.timeStamp).toRelative() }}
            </p>
        </div>
    </div>
</template>

<script lang="ts">
import {PropType} from 'vue';
import {EliminationFeedItem} from '@/utils/types';

export default {
    props: {
        item: Object as PropType<EliminationFeedItem>
    }
}
</script>

<script setup lang="ts">
import {DateTime} from 'luxon';
</script>
