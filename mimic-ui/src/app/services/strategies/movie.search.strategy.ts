import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {SearchStrategy} from "../search.service";
import {Movie, MovieService} from "../movie.service";

export class MovieSearchResult {

    constructor(public id: number, public title: string, public year: number, public genres: string[], public image: string) {
    }
}

@Injectable()
export class MovieSearchStrategy implements SearchStrategy {

    private limit: number = 5;

    constructor(private movieService: MovieService) {
    }

    getId(): string {
        return "movies";
    }

    search(query: string): Observable<Object[]> {
        console.log("MovieSearchStrategy: search for " + query);
        query = query.toLowerCase();

        return new Observable<Object[]>(observer => {
            setTimeout(() => {
                return new Promise((resolve, reject) => {
                    this.movieService.getMovies().then(
                        data => {
                            let matches: MovieSearchResult[] = data
                                .filter(m => this.matches(m, query))
                                .map(m => new MovieSearchResult(m.id, m.title, m.year, m.genres, m.image));
                            // limit number of items
                            matches = matches.slice(0, Math.min(this.limit, matches.length));
                            observer.next(matches);
                        },
                        error => reject(error)
                    );
                });
            }, 200);
        });
    }

    private matches(movie: Movie, query: string): boolean {
        if (!query || !movie) {
            return false;
        }
        return query.split(" ")
            .every(term =>
                this.containsTerm(movie.title, term) || // by title
                String(movie.year) == term || // by year
                movie.genres.some(genre => this.containsTerm(genre, term) // by genre
                ));
    }

    private containsTerm(text: string, term: string): boolean {
        if (!text || !term) {
            return false;
        }
        return text.toLowerCase()
            .split(" ")
            .some(s => s.startsWith(term.toLowerCase()));
    }
}