<template>
    <div
        class="flex items-center justify-center p-6 2xl:p-8 relative border border-gray-300/10"
        :class="currentTime.time.hasSame(date, 'day') ? 'bg-primary' : !item ? 'bg-base-200' : ''"
    >
        <span class="absolute text-sm text-secondary top-2.5 left-3">
            {{ date.day }}
        </span>
        <p v-if="item" class="text-sm 2xl:text-base">
            <strong v-if="item.announcement">
                {{ item.announcement }}<template v-if="item.restriction">: </template>
            </strong>
            <template v-if="item.restriction">
                <em v-if="item.restrictionType === 'SAFE'">To stay safe,</em>
                <em v-else>To eliminate others,</em>
                {{ lowercaseFirstLetter(item.restriction) }}
            </template>
        </p>
    </div>
</template>

<script lang="ts">
import type {PropType} from 'vue';
import type {DayInfo} from '@/utils/calendar';
import {DateTime} from 'luxon';

export default {
    name: "CalendarCell",
    props: {
        date: Object as PropType<DateTime>,
        item: Object as PropType<DayInfo | null>
    }
}
</script>

<script setup lang="ts">
import {useCurrentTimeStore} from '@/store/time';
import {lowercaseFirstLetter} from '@/utils/strings';

const currentTime = useCurrentTimeStore();
</script>
