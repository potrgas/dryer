<template>
  <div>
    <Row>
     
      <Card dis-hover>
        <div class="page-body">
          <Form slot="filter" ref="queryForm" :label-width="70" label-position="left" inline>
            <Row :gutter="4">
              <Col span="18">
              <FormItem label="商品名:">
                <Input v-model="filters.productName" />
              </FormItem>
              <FormItem label="商品编号:">
                <Input v-model="filters.productName" />
              </FormItem>
              <FormItem label="商品类型:">
                <Select clearable v-model="filters.productType" style="width:160px">
                  <Option v-for="item in cates" :value="item.name" :key="item.id">{{ item.name }}</Option>
                </Select>
              </FormItem>
              <FormItem label="是否售卖:">
                <Input v-model="filters.productName" />
              </FormItem>
              </Col>
              <Col span="6">
              <Button icon="ios-search" type="primary" size="large" @click="init" class="toolbar-btn">查找</Button>
              </Col>
            </Row>
          </Form>
          <SaleTable ref="table" :filters="filters" :type="'product'" :columns="columns"></SaleTable>
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
import Product from "@/store/entities/product";
import Modify from "./modify.vue";

@Component({
  components: {
    SaleTable,
    Modify
  }
})
export default class ProductC extends AbpBase {
  filters: Object = {
    productName: ""
  };
  p: any = {
    modify: this.hasPermission("product:modify"),
    delete: this.hasPermission("product:delete"),
    list: this.hasPermission("product:list"),
    first: this.hasPermission("product:first"),
    batch: this.hasPermission("product:batch")
  };
  get cates() {
    return this.$store.state.category.list;
  }
  ModalShow: boolean = false;
  columns: Array<any> = [
    {
      type: "selection",
      width: 60,
      align: "center"
    },
    {
      title: "客户姓名",
      key: "productName"
    },
    {
      title: "电话",
      key: "productNum"
    },
    {
      title: "地址",
      key: "productType"
    },
    {
      title: "织物类型",
      key: "productType"
    },
    {
      title: "状态",
      key: "productType"
    },
    {
      title: "服务人员姓名",
      key: "productType"
    },
    {
      title: "服务人员电话",
      key: "productType"
    },

    {
      title: "创建时间",
      render: (h: any, params: any) => {
        return h(
          "span",
          new Date(params.row.creationTime).toLocaleDateString()
        );
      }
    }
  ];
  Create() {
    var u = new Product();
    this.$store.commit("product/edit", u);
    this.ModalShow = true;
  }
  init() {
    var t: any = this.$refs.table;
    t.getpage();
  }
  async batchDelete() {
    var t: any = this.$refs.table;
    if (t.selections) {
      this.$Modal.confirm({
        title: "删除提示",
        content: `确认要删除${t.selections.length}条数据么`,
        okText: "是",
        cancelText: "否",
        onOk: async () => {
          await this.$store.dispatch({
            type: "product/batch",
            data: t.selections
          });
          await this.init();
        }
      });
    }
  }
  Modify() {
    this.ModalShow = true;
  }
  async created() {
    this.$store.dispatch({
      type: "category/all",
      data: {}
    });
  }
}
</script>