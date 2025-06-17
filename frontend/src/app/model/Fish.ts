import { Level } from "./Level";

export type Fish = {
    name : string;
    averageWeight : number;
    imageUrl? : string;
    level : Level;
}
