<template>
    <li class="flex gap-4 items-center">
        <!-- src="https://placeimg.com/192/192/people?e=p" -->
        <Avatar :placeholder="(forename.charAt(0) + surname.charAt(0)).toUpperCase()" />
        <p class="font-medium">{{ forename }} {{ surname }}</p>
        <p class="text-secondary italic">{{ eliminations }} eliminations</p>
    </li>
</template>

<script>
export default {
    name: "LeaderboardUser",
    props: {
        forename: String,
        surname: String,
        eliminations: Number
    }
}
</script>
