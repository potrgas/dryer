<template>
  <div>
    <Row>
     
      <Card dis-hover>
        <div class="page-body">
          <Form slot="filter" ref="queryForm" :label-width="70" label-position="left" inline>
            <Row :gutter="4">
              <Col span="18">
              <FormItem label="账户:">
                <Input v-model="filters.account" />
              </FormItem>
              </Col>
              <Col span="6">
              <Button icon="ios-search" type="primary" size="large" @click="init" class="toolbar-btn">查找</Button>
              </Col>
            </Row>
          </Form>
          <SaleTable ref="table" :filters="filters" :type="'operater'" :columns="columns"></SaleTable>
        </div>
      </Card>
    </Row>

    <modify v-model="ModalShow" @save-success="init"></modify>
  </div>
</template>
<script lang="ts">
import { Component, Vue, Inject, Prop, Watch } from "vue-property-decorator";
import SaleTable from "@/components/saletable.vue";
import AbpBase from "@/lib/abpbase";
import PageRequest from "../../store/entities/page-request";
import Util from "../../lib/util";
import Operater from "@/store/entities/operater";
import Modify from "./modify.vue";

@Component({
  components: {
    SaleTable,
    Modify
  }
})
export default class OperaterControl extends AbpBase {
  filters: Object = {
    account: ""
  };
  ModalShow: boolean = false;
  columns: Array<any> = [
    {
      type: "selection",
      width: 60,
      align: "center"
    },
    {
      title: "唯一标识",
      key: "openId"
    },
    {
      title: "账户",
      key: "account"
    },
    {
      title: "密码",
      key: "",
      render: (h: any, params: any) => {
        return h("span", "*****");
      }
    },
    {
      title: "操作",
      key: "Actions",
      width: 150,
      render: (h: any, params: any) => {
        var ed = h(
          "Button",
          {
            props: {
              type: "primary",
              size: "small"
            },
            style: {
              marginRight: "5px"
            },
            on: {
              click: () => {
                this.$store.dispatch({
                  type: "operater/get",
                  data: params.row.id
                });
                this.Modify();
              }
            }
          },
          "编辑"
        );

        var t = [ed];
        return h("div", t);
      }
    }
  ];
  Create() {
    var u = new Operater();
    this.$store.commit("operater/edit", u);
    this.ModalShow = true;
  }
  init() {
    var t: any = this.$refs.table;
    t.getpage();
  }

  Modify() {
    this.ModalShow = true;
  }
  async created() {}
}
</script>