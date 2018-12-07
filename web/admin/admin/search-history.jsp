<v-card>
    <v-toolbar flat>
        <v-toolbar-title>Search History</v-toolbar-title>
    </v-toolbar>
    <template>
        <v-data-table
                :headers="headers"
                :items="history"
                class="elevation-1"
                :pagination.sync="pag"
        >
            <template slot="items" slot-scope="props">
                <td>{{ props.item.timestamp }}</td>
                <td>{{ props.item.keyword }}</td>
            </template>
        </v-data-table>
    </template>
</v-card>