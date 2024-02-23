import { Competition } from "./Competition";
import { Member } from "./Member";


export type Ranking = {
    id: number;
    rank: number;
    score: number;
    member : Member;
    competition : Competition;
    member_id : number;
    competition_id : string;
}