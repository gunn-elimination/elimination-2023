export type EliminationUser = {
    email: string,
    forename: string,
    surname: string,
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
    timestamp: string
}
