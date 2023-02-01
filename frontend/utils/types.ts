export type EliminationUser = {
    email: string,
    forename: string,
    surname: string,
    eliminatedCount: number,
}

export type MeEliminationUser = EliminationUser & {
    eliminated: EliminationUser[]
}

export type Announcement = {
    id: number,
    title: string,
    body: string,
    active: boolean,
    startDate: number,
    endDate: number
}

export type EliminationFeedItem = {
    eliminated: EliminationUser,
    eliminator: EliminationUser,
    timeStamp: string
}
