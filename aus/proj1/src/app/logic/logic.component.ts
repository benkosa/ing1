import { Component, OnInit } from "@angular/core";
import { BSTData } from "../BST/BSTData";
import { BSTree } from "../BST/BSTree";

@Component({
  selector: "app-logic",
  templateUrl: "./logic.component.html",
  styleUrls: ["./logic.component.scss"],
})
export class LogicComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {
    this.testTree();
  }

  testTree() {
    class Element implements BSTData<number, string> {
      constructor(public data: number) {}
      compare(data: number): string {
        if (data < this.data) return "<";
        if (data > this.data) return ">";
        return "=";
      }
    }

    const el1 = new Element(50);
    const el2 = new Element(20);
    const el3 = new Element(10);
    const el4 = new Element(80);

    const tree: BSTree<Element> = new BSTree<Element>();

    tree.insert(el1);
    tree.insert(el1);
    tree.insert(el2);
    tree.insert(el3);
    tree.insert(el4);
  }
}
