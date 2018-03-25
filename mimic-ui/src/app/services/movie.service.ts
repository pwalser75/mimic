import {Injectable} from "@angular/core";
import {Http} from "@angular/http";

export interface Movie {
    id: number;
    title: string;
    year: number;
    genres: string[];
    rating: number;
    image: string;
    plot: string;
    youtubeTrailerId: string;
}

@Injectable()
export class MovieService {

    private movies: Promise<Movie[]>;

    constructor(http: Http) {

        let resource: string = "data/movies.json";

        this.movies = new Promise((resolve, reject) => {
            http.get(resource)
                .map((res: any) => res.json()).subscribe(
                data => resolve(data),
                error => reject(error)
            );
        });
    }

    getMovies(): Promise<Movie[]> {
        return this.movies;
    }

    getMovie(id: Number): Promise<Movie> {
        return new Promise((resolve, reject) => {
            this.getMovies().then(
                data => resolve(data.find(m => m.id === id)),
                error => reject(error)
            );
        });
    }
}
