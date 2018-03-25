import {Component, OnDestroy, OnInit} from "@angular/core";
import {Title} from "@angular/platform-browser";
import {Movie, MovieService} from "../../services/movie.service";
import {ActivatedRoute} from "@angular/router";

@Component({
    selector: 'movie',
    templateUrl: 'movie-detail.html'
})
export class MovieDetailComponent implements OnInit, OnDestroy {

    movie: Movie;
    private sub: any;

    constructor(private movieService: MovieService, private route: ActivatedRoute, private titleService: Title) {

    }

    ngOnInit(): void {
        this.sub = this.route.params.subscribe(params => {
            var id = +params['id']; // (+) converts string 'id' to a number
            this.movieService.getMovie(id).then(
                data => {
                    this.movie = data;
                    this.titleService.setTitle(this.movie ? this.movie.title + "(" + this.movie.year + ")" : 'no movie');
                },
                error => console.log("could not load movie #" + id)
            );
        });
    }

    ngOnDestroy(): void {
        this.sub.unsubscribe();
    }
}
