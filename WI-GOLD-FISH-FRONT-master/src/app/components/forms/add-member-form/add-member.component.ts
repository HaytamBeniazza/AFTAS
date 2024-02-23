import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import {Competition } from 'src/app/model/Competition';
import {Member} from 'src/app/model/Member';
import {Ranking } from 'src/app/model/Ranking';
import { CompetitionService } from 'src/app/services/competition.service';
import { MemberService } from 'src/app/services/member.service';
import { RankingService } from 'src/app/services/ranking.service';
import { AlertService } from '../../alerts/alert-service.service';
import { IndentityDocumentType } from 'src/app/enum/IndentityDocumentType';

@Component({
  selector: 'app-add-member',
  templateUrl: './add-member.component.html',
  styleUrls: ['./add-member.component.css']
})
export class AddMemberComponent {
  @Input() visible: boolean = false;
  members: Member[] = [];
  showDialog() {
    this.visible = true;
  }
  @Input() competition: Competition = {} as Competition;
  @Input() selectedMembers: Member[] = [];
  @Input() ranking: Ranking = {} as Ranking;
  memberForm: FormGroup;
  constructor(private memberService: MemberService, private rankingService: RankingService, private competitionService: CompetitionService, private alertService: AlertService) {
    this.memberForm = new FormBuilder().group({
      name: ['', Validators.required],
      familyName: ['', Validators.required],
      accessionDate: ['', Validators.required],
      nationality: ['', Validators.required],
      indentityDocumentType: [IndentityDocumentType.CIN, Validators.required],
      indentityNumber: ['', Validators.required],
    });
  }
  ngAfterViewInit() {
    this.memberService.members.subscribe(
      (members) => {
        this.members = members;
      }
    );
  }
  onSubmit() {
    this.selectedMembers.forEach(
      (member) => {
        this.ranking.member_id = member.id;
        this.ranking.competition_id = this.competition.code;
        this.ranking.score = 0;
        this.ranking.rank = 0;
        this.rankingService.save(this.ranking);
      },
    )
    this.alertService.showMsg('Members added successfully');
  }
  onSubmitAddMember(){
    this.memberService.save(this.memberForm.value);
  }
  onChangeSelectedMembers() {
    if (this.selectedMembers.length > (this.competition.numberOfParticipants - this.competition.ranking.length)) {
      this.alertService.showMsg('Number of participants exceeded');
      this.selectedMembers.pop();
    }
    this.selectedMembers.forEach(
      (member) => {
        if (this.competition.ranking.find(ranking => ranking.member.id == member.id)) {
          this.alertService.showMsg('Member already exists');
          this.selectedMembers.pop();
        }
      },
    )
  }
  getEnumKeys(): string[] {
    return Object.keys(IndentityDocumentType);
  }
}
