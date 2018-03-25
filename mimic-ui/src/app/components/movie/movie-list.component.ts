import {Component, OnInit} from "@angular/core";
import {Movie, MovieService} from "../../services/movie.service";
import {LoadingBarService} from "../../services/loading-bar.service";

@Component({
    selector: 'movies',
    templateUrl: 'movie-list.html'
})
export class MovieListComponent implements OnInit {

    movies: Movie[];

    constructor(private movieService: MovieService, private loadingService: LoadingBarService) {
    }

    ngOnInit(): void {
        this.loadingService.start();
        this.movieService.getMovies().then(
            data => {
                this.movies = data;
                this.loadingService.stop();
            },
            error => {
                console.log(error),
                    this.loadingService.stop();
            }
        );
    }
}

