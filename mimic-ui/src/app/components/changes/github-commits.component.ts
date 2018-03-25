import {Component, Input, OnInit} from "@angular/core";
import {GithubService} from "../../services/github.service";
import {LoadingBarService} from "../../services/loading-bar.service";

@Component({
    selector: 'github-commits',
    templateUrl: 'github-commits.html'
})
export class GithubCommitsComponent implements OnInit {

    @Input() url: string;
    commits: any;

    constructor(private githubService: GithubService, private loadingService: LoadingBarService) {
    }

    ngOnInit(): void {
        if (this.url) {
            this.loadingService.start();
            this.githubService.getCommits(this.url)
                .subscribe(
                    data => {
                        this.commits = data;
                        this.loadingService.stop();
                    },
                    error => {
                        console.log(error),
                            this.loadingService.stop();
                    }
                );
        }
    }
}