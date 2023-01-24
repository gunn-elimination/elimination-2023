export const calendar: {[key: string]: DayInfo | null} = {
    "2023-01-22": null,
    "2023-01-23": null,
    "2023-01-24": null,
    "2023-01-25": {
        announcement: 'Plushie Handouts @ Senior Quad, Lunch, Afterschool'
    },
    "2023-01-26": {
        announcement: 'Plushie Handouts @ Senior Quad, Lunch, Afterschool'
    },
    "2023-01-27": {
        announcement: 'GAME STARTS 12 AM',
        restriction: 'Players must hold the animal with their right hand.',
        restrictionType: 'SAFE'
    },
    "2023-01-28": null,
    "2023-01-29": {
        announcement: 'On weekends, players must abide by all rules and must carry a plushie to be safe.'
    },
    "2023-01-30": {
        restriction: 'Players must be holding the animal with both hands.',
        restrictionType: 'SAFE'
    },
    "2023-01-31": {
        restriction: 'Players must be under a roof or overhang.',
        restrictionType: 'SAFE'
    },
    "2023-02-01": {
        announcement: 'Targets Change 8 PM',
        restriction: 'Players must be touching a wall.',
        restrictionType: 'SAFE'
    },
    "2023-02-02": {
        restriction: 'Players must be holding their animal ABOVE their shoulder.',
        restrictionType: 'SAFE'
    },
    "2023-02-03": {
        restriction: 'Players must be holding their animal BELOW their waist.',
        restrictionType: 'SAFE'
    },
    "2023-02-04": null,
    "2023-02-05": {
        announcement: 'For this week, nobody is safe: there are only requirements for eliminating others.'
    },
    "2023-02-06": {
        restriction: 'Players must be (visibly) wearing RED.',
        restrictionType: 'ELIMINATE'
    },
    "2023-02-07": {
        restriction: 'Players must wear a HAT.',
        restrictionType: 'ELIMINATE'
    },
    "2023-02-08": {
        announcement: 'Targets Change 8 PM',
        restriction: 'Players must be wearing JEANS.',
        restrictionType: 'ELIMINATE'
    },
    "2023-02-09": {
        restriction: 'Players must be SITTING DOWN.',
        restrictionType: 'ELIMINATE'
    },
    "2023-02-10": {
        announcement: 'NO ONE IS SAFE (anyone can be tagged anytime even with animal).'
    },
    "2023-02-11": null
}

export type DayInfo = {
    announcement?: string,
    restriction?: string,
    restrictionType?: 'SAFE' | 'ELIMINATE'
}
